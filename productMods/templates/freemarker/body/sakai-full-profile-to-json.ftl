<#import "lib-list.ftl" as l>
<#import "lib-properties.ftl" as p>
<#import "lib-sequence.ftl" as s>
<#import "lib-datetime.ftl" as dt>

<#assign propertyGroups = individual.propertyList>
<#assign core = "http://vivoweb.org/ontology/core#">
<#assign nameForOtherGroup = "other">
{
    "basic": {
        "firstName": "${individual.firstName}",                                                                                              
        "lastName": "${individual.lastName}",
        "uri": "${individual.uri}" 
    },
    <#list propertyGroups.all as group>
        <#assign groupName = group.getName(nameForOtherGroup)>
        <#assign verbose = (verbosePropertySwitch.currentValue)!false>  
        <#assign groupNameHtmlId = p.createPropertyGroupHtmlId(groupName) >
        
        "${groupNameHtmlId}" : {
            <#list group.properties as property>
                <#assign t=property.template!"none">
                <#assign subGroupNameHtmlId = p.createPropertyGroupHtmlId(property.name) >
                <#-- "${subGroupNameHtmlId}-${property.type}-${t}": -->
                "${subGroupNameHtmlId}": 
                            <#if property.type == "data">
                                [
                                <#list property.statements as statement>
                                   <@showStatement statement />
                                   <#if statement_has_next>,</#if>
                                </#list>
                                <#if property.statements?size == 0>""</#if>
                                ]
                                <#if property_has_next>,</#if>
                            <#else>
                                [
                                <#-- List the statements for each property -->
                                <#if property.collatedBySubclass> <#-- collated -->

                                    <#list property.subclasses as subclass>
                                        <#assign subclassName = subclass.name!>
                                        <#if subclassName?has_content>
                                                    <#list subclass.statements as statement>
                                                        <@showAuthorship statement subclassName />
                                                        <#if statement_has_next>,</#if>
                                                    </#list>
                                        <#else>
                                            <#-- If not in a real subclass, the statements are in a dummy subclass with an
                                                 empty name. List them in the top level ul, not nested. -->
                                        {}
                                        </#if>
                                        <#if subclass_has_next>,</#if>
                                    </#list>


                                <#else> <#-- uncollated -->
                                    <#list property.statements as statement>
                                       <#if property.template == "propStatement-personInPosition.ftl">
                                         <@showPosition statement />
                                       <#elseif property.template == "propStatement-default.ftl">
                                           "${statement.label!statement.localName?trim!}"
                                       <#elseif property.template == "propStatement-dataDefault.ftl">
                                           "${statement.value?trim}"
                                       </#if>
                                       <#if statement_has_next>,</#if>
                                    </#list>
                                </#if>
                                ]
                                <#if property_has_next>,</#if>
                           </#if>
            </#list>
        }
        <#if group_has_next>,</#if>
    </#list>
}
<#macro showStatement statement>
    "${statement.value?trim!}"
</#macro>


<#macro showPosition statement>
    {
      "organization": "${statement.orgName!"bummer"}",
      "organization_uri": "${statement.uri("org")}",
      "title": "${statement.positionTitle!"nope"}"
    }
</#macro>

<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showAuthorship statement type>
    <#assign class="Paper">
    <#local citationDetails>
        <#if statement.subclass??>
                "journal": "${statement.journal!}",
                "volume": "${statement.volume!""}",
                "startpage": "${statement.startPage!""}",
                "endpage": "${statement.endPage!""}",
                "appearsin": "${statement.appearsIn!}",
                "partof": "${statement.partOf!}",
                "editor": "${statement.editor!}",
                "locale": "${statement.locale!}",
                "publisher": "${statement.publisher!}"
            <#if statement.subclass?contains("Article")>
                <#assign class="Article">
                <#if statement.journal??>
                    <#assign class="Journal article">
                </#if>
            <#elseif statement.subclass?contains("Chapter")>
                <#assign class="Chapter">
            <#elseif statement.subclass?contains("Book")>
                <#assign class="Book">
            </#if>
        </#if>
    </#local>
{
  "title": "${statement.infoResourceName}",  
  "uri": "${profileUrl(statement.uri("infoResource"))}",
  "type": "${type}",
  "class": "${class}",
  ${citationDetails}
}
</#macro>


