<#import "lib-list.ftl" as l>
<#import "lib-properties.ftl" as p>
<#assign propertyGroups = individual.propertyList>
<#assign core = "http://vivoweb.org/ontology/core#">
<#assign nameForOtherGroup = "other">
{
    "basic": {
        "firstName": "${individual.firstName}",                                                                                              
        "lastName": "${individual.lastName}",
        "email": "${individual.primaryEmail}" 
    },
    <#list propertyGroups.all as group>
        <#assign groupName = group.getName(nameForOtherGroup)>
        <#assign verbose = (verbosePropertySwitch.currentValue)!false>  
        <#assign groupNameHtmlId = p.createPropertyGroupHtmlId(groupName) >
        
        "${groupNameHtmlId}" : {
            <#list group.properties as property>
                <#assign subGroupNameHtmlId = p.createPropertyGroupHtmlId(property.name) >
                "${subGroupNameHtmlId}": [
                         <#-- List the statements for each property -->
                        <ul class="property-list" role="list" id="${property.localName}List">
                            <#-- data property -->
                            <#if property.type == "data">
                                <@p.dataPropertyList property false />
                            <#-- object property -->
                            <#else>
                                <@p.objectProperty property false />
                            </#if>
                        </ul>                    
                ]
            </#list>
        }
    </#list>
}   
