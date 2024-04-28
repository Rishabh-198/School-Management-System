//This was earlier done by JDBC. ee below for updated code using JPA
// package com.RS.SMS.service;
//
//import com.RS.SMS.constants.smsConstants;
//import com.RS.SMS.model.Contact;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.annotation.ApplicationScope;
//import com.RS.SMS.Repository.ContactRepository;
//import java.time.LocalDateTime;
//import java.util.List;
//
///*
//@Slf4j, is a Lombok-provided annotation that will automatically generate an SLF4J
//Logger static property in the class at compilation time.
//* */
//@Slf4j
//@Service
//public class ContactService {
//
//    @Autowired
//    private ContactRepository contactRepository;
//
//    /**
//     * Save Contact Details into DB
//     * @param contact
//     * @return boolean
//     */
//    public boolean saveMessageDetails(Contact contact){
//        boolean isSaved = false;
//        contact.setStatus(smsConstants.OPEN);
//        contact.setCreatedBy(smsConstants.ANONYMOUS);
//        contact.setCreatedAt(LocalDateTime.now());
//        int result = contactRepository.saveContactMsg(contact);
//        if(result>0) {
//            isSaved = true;
//        }
//        return isSaved;
//    }
//
//
//    public List<Contact> findMsgsWithOpenStatus(){
//        List<Contact> contactMsgs = contactRepository.findMsgsWithStatus(smsConstants.OPEN);
//        return contactMsgs;
//    }
//
//    public boolean updateMsgStatus(int contactId, String updatedBy){
//        boolean isUpdated = false;
//        int result = contactRepository.updateMsgStatus(contactId,smsConstants.CLOSE, updatedBy);
//        if(result>0) {
//            isUpdated = true;
//        }
//        return isUpdated;
//    }
//
//
//}
package com.RS.SMS.service;
import com.RS.SMS.Repository.ContactRepository;
import com.RS.SMS.config.SMSProps;
import com.RS.SMS.constants.smsConstants;
import com.RS.SMS.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    SMSProps smsProps;


    /**
     * Save Contact Details into DB
     * @param contact
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        contact.setStatus(smsConstants.OPEN);
//        We are doing this automatically in BaseEntity class
//        contact.setCreatedBy(smsConstants.ANONYMOUS);
//        contact.setCreatedAt(LocalDateTime.now());
          Contact savedContact = contactRepository.save(contact);
        if(null != savedContact && savedContact.getContactId()>0) {
            isSaved = true;
        }
        return isSaved;
    }


    public Page<Contact> findMsgsWithOpenStatus(int pageNum,String sortField, String sortDir){
       //now we have defined this as prop, so we dont need it now. int pageSize = 5;

        int pageSize = smsProps.getPageSize();
        if(null!=smsProps.getContact() && null!=smsProps.getContact().get("pageSize")){
            pageSize = Integer.parseInt(smsProps.getContact().get("pageSize").trim());
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        Page<Contact> msgPage = contactRepository.findByStatusWithQuery(
                smsConstants.OPEN,pageable);
        return msgPage;
    }

    public boolean updateMsgStatus(int contactId){
       /* This was done using JPA. Now we haev written JPQL query in Repository class.
       boolean isUpdated = false;
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent(contact1 -> {
            contact1.setStatus(smsConstants.CLOSE);
            //We are doing this automatically in BaseEntity class
            //contact1.setUpdatedBy(updatedBy);
//            contact1.setUpdatedAt(LocalDateTime.now());
        });
        Contact updatedContact = contactRepository.save(contact.get());
        if(null != updatedContact && updatedContact.getUpdatedBy()!=null) {
            isUpdated = true;
        }
        return isUpdated;
    */
        boolean isUpdated = false;
        int rows = contactRepository.updateMsgStatusNative(smsConstants.CLOSE,contactId);
        if(rows > 0) {
            isUpdated = true;
        }
        return isUpdated;
    }

}
