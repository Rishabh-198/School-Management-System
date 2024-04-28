package com.RS.SMS.Repository;

import com.RS.SMS.model.SmsClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsClassRepository extends JpaRepository<SmsClass, Integer> {

}