package org.example;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.List;
import javax.inject.Inject;
import org.example.jpadomain.Company;
import org.example.jpadomain.Contact;
import org.example.service.deltaspike.DeltaSpikeContactService;
import org.example.service.querydsl.QueryDslContactService;
import org.example.service.springdata.SpringDataContactService;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@CDIUI("")
@Theme("valo")
@Title("Phonebook")
public class VaadinUI extends UI {

    @Inject
    DeltaSpikeContactService service;
    //QueryDslContactService service;
    //SpringDataContactService service;

    @Inject // With Vaadin CDI one can also inject basic ui components
    ContactForm form;

    // Instantiate and configure a Table to list PhoneBookEntries
    MTable<Contact> entryList = new MTable<>(Contact.class)
            .withHeight("350px")
            .withFullWidth()
            .withProperties("name", "email");

    // Instanticate buttons, hook directly to listener methods in this class
    Button addNew = new MButton(FontAwesome.PLUS, this::addNew);
    TextField filter = new MTextField().withInputPrompt("filter by name");
    TypedSelect<Company> companyFilter = new TypedSelect(Company.class).
            withSelectType(OptionGroup.class);

    private void addNew(Button.ClickEvent e) {
        entryList.setValue(null);
        final Contact contact = new Contact();
        contact.setCompany(companyFilter.getValue());
        editEntry(contact);
    }

    private void deleteSelected(Button.ClickEvent e) {
        service.delete(entryList.getValue());
        listEntries();
        entryList.setValue(null);
    }

    private void listEntries(String filter) {
        List<Contact> list = new LazyList<>(
                firstrow -> service.findPageByCompanyAndName(companyFilter.getValue(),
                        filter, firstrow, LazyList.DEFAULT_PAGE_SIZE),
                () -> {
                    return service.countByCompanyAndName(companyFilter.
                            getValue(), filter).intValue();
                }, LazyList.DEFAULT_PAGE_SIZE);
        entryList.setBeans(list);
    }

    private void listEntries() {
        listEntries(filter.getValue());
    }

    public void entryEditCanceled(Contact entry) {
        editEntry(entryList.getValue());
    }

    public void entrySelected(MValueChangeEvent<Contact> event) {
        editEntry(event.getValue());
    }

    /**
     * Assigns the given entry to form for editing.
     *
     * @param entry
     */
    private void editEntry(Contact entry) {
        if (entry == null) {
            form.setVisible(false);
        } else {
            boolean persisted = entry.getId() != null;
            if (persisted) {
                // find a fresh entity
                entry = service.refreshEntry(entry);
            }
            form.setEntity(entry);
            form.focusFirst();
        }
    }

    public void entrySaved(Contact value) {
        try {
            service.save(value);
            form.setVisible(false);
        } catch (Exception e) {
            // Most likely optimistic locking exception
            Notification.show("Saving entity failed!", e.
                    getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
        }
        // deselect the entity
        entryList.setValue(null);
        // refresh list
        listEntries();
    }

    @Override
    protected void init(VaadinRequest request) {

        companyFilter.setOptions(service.findCompanies());
        companyFilter.selectFirst();
        companyFilter.addMValueChangeListener(e -> listEntries());

        // Add some event listners, e.g. to hook filter input to actually 
        // filter the displayed entries
        filter.addTextChangeListener(e -> listEntries(e.getText()));
        entryList.addMValueChangeListener(this::entrySelected);
        form.setSavedHandler(this::entrySaved);
        form.setResetHandler(this::entryEditCanceled);

        setContent(
                new MVerticalLayout(
                        new Header("PhoneBook"),
                        new MHorizontalLayout(addNew, filter, companyFilter)
                        .alignAll(Alignment.MIDDLE_LEFT),
                        new MHorizontalLayout(entryList, form).withFullWidth()
                )
        );

        // List all entries and select first entry in the list
        listEntries();
        entryList.setValue(entryList.firstItemId());
    }
}
