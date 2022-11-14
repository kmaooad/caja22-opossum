package edu.kmaooad.service;

import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupTemplateRepository;
import edu.kmaooad.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupTemplateService {
    @Autowired
   private GroupTemplateRepository repository;


    public boolean addGroupTemplate (GroupTemplate groupTemplate){
        // яка перевірка на унікальність в бд?

        return false;
    }

    public boolean updateGroupTemplate (GroupTemplate groupTemplate){
        // яка перевірка на унікальність в бд?

        return false;
    }


    public boolean deleteGroupTemplate (String groupTemplateId){

     //   return repository.delete(groupTemplateId);

        return false;
    }



}
