package org.example.service.deltaspike;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.example.jpadomain.Company;
import org.example.jpadomain.Contact;

/**
 * EJB to hide JPA related stuff from the UI layer.
 */
@Stateless
public class DeltaSpikeContactService {

    @Inject
    ContactRepository repository;

    @Inject
    CompanyRepository companyRepository;

    public DeltaSpikeContactService() {
    }

    public void save(Contact entry) {
        repository.save(entry);
    }

    public void delete(Contact value) {
        repository.remove(value);
    }

    public List<Contact> findByCompanyAndName(Company company, String filter) {
        return repository.findByCompanyAndNameLikeIgnoreCase(company,
                "%" + filter + "%").getResultList();
    }

    public List<Contact> findPageByCompanyAndName(Company company, String filter,
            int firstrow, int maxrows) {
        return repository.findByCompanyAndNameLikeIgnoreCase(company,
                "%" + filter + "%").firstResult(firstrow).maxResults(maxrows).
                getResultList();
    }

    public Long countByCompanyAndName(Company company, String filter) {
        return repository.findByCompanyAndNameLikeIgnoreCase(company,
                "%" + filter + "%").count();
    }

    public List<Company> findCompanies() {
        return companyRepository.findAll();
    }

    public Contact refreshEntry(Contact entry) {
        return repository.findBy(entry.getId());
    }

}
