package org.example.service.deltaspike;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.QueryResult;
import org.apache.deltaspike.data.api.Repository;
import org.example.jpadomain.Company;
import org.example.jpadomain.Contact;

@Repository(forEntity = Contact.class)
public interface ContactRepository extends EntityRepository<Contact, Long> {

    public List<Contact> findByCompany(Company company);

    public QueryResult findByCompanyAndNameLikeIgnoreCase(Company company,
            String string);
}
