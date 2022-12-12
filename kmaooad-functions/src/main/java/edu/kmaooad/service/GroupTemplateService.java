package edu.kmaooad.service;

import edu.kmaooad.model.Group;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.repositories.GroupTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupTemplateService {
    @Autowired
    private GroupTemplateRepository groupTemplateRepository;

    // Get all templates from db
    public List<GroupTemplate> getAllTemplates(){
        return groupTemplateRepository.findAll();
    }

    /**
     * @param groupTemplate  - group template to add
     * @return added template updated, if already exists - throws exception
     */
    public GroupTemplate addGroupTemplate(GroupTemplate groupTemplate) throws ServiceException {

        if(groupTemplate.getName()==null&&groupTemplate.getYear() == null&& groupTemplate.getGrade()==null){
            throw new ServiceException("Failed to add group template: "+groupTemplate + " all fields are null");

        }
        if (null == groupTemplateRepository.findByNameAndYearAndGrade(groupTemplate.getName(), groupTemplate.getYear(), groupTemplate.getGrade())){
            groupTemplateRepository.save(groupTemplate);
            return groupTemplate;
        }else{
            throw new ServiceException("Failed to add group template: "+groupTemplate + " group with such values exists in database");
        }
    }

    /**
     * @param groupTemplateUpdate  - group template to updated
     * @return group template updated, if not found - throws exception
     */
    public GroupTemplate updateGroupTemplate(GroupTemplate groupTemplateUpdate) throws ServiceException {
        Optional<GroupTemplate> group = groupTemplateRepository.findById(groupTemplateUpdate.getId());
        if (null != groupTemplateRepository.findByNameAndYearAndGrade(groupTemplateUpdate.getName(), groupTemplateUpdate.getYear(), groupTemplateUpdate.getGrade())){
            throw new ServiceException("Failed to add group template: "+groupTemplateUpdate + " group with such values exists in database");
        }
        if (group.isPresent()) {
            GroupTemplate groupFound = group.get();
            groupFound.setName(groupTemplateUpdate.getName());
            groupFound.setGrade(groupTemplateUpdate.getGrade());
            groupFound.setYear(groupTemplateUpdate.getYear());
             groupTemplateRepository.save(groupFound);
            return groupFound;
        }else{
            throw new ServiceException("Failed to update group template: "+groupTemplateUpdate + " group with such id doesn`t exists in database");
        }
    }

    /**
     * @param groupTemplateId  - group template to delete
     * @return group template deleted, if not found - throws exception
     */
    public GroupTemplate deleteGroupTemplate(String groupTemplateId) throws ServiceException {
        Optional<GroupTemplate> group = groupTemplateRepository.findById(groupTemplateId);
        if (group.isPresent()) {
            GroupTemplate groupFound = group.get();
            groupTemplateRepository.delete(groupFound);
            return groupFound;
        }else{
            throw new ServiceException("Failed to delete group template with id: "+groupTemplateId + " group with such id doesn`t exists in database");
        }
    }


}
