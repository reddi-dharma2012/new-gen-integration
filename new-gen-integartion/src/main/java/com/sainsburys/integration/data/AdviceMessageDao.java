package com.sainsburys.integration.data;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sainsburys.integration.models.AdviceMessage;

//@Repository
public interface AdviceMessageDao extends CrudRepository<AdviceMessage, String> {
    
//    @Override
//    void delete(AdviceMessage message);
//    
//    @Override
//    Iterable<AdviceMessage> findAll();
//    
//    @Override
//    AdviceMessage save(AdviceMessage message);
    
}