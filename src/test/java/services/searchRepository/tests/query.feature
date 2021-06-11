##
# @author mlee
##

@searchRepo @query
Feature: Search Repository Service - Tests that involve querying

  Background:
    * callonce read(globalCommonPath)
    * url ghURL + 'search/repositories'

  Scenario: Search repos for user yanglbme and ensure each repo returned is owned by yanglbme
    * params {q: 'user:yanglbme', sort: 'forks', order: 'asc', per_page: '10'}
    When method GET
    Then status 200
    # Get the ownerSchemas json resource, and match user yanglbme with each items[*].owner object in the response
    * def ownerSchemas = read('classpath:services/searchRepository/templates/response/ownerSchema.json')
    And match each response..owner == ownerSchemas.yanglbme


  Scenario: Ensure all urls in a specific user's repo belongs to them
    * params {q: 'user:yanglbme', per_page: '1'}
    When method GET
    Then status 200
    * def fullName = response.items[0].full_name
    # define urls json, and replace the token in the json dynamically with the extracted fullName from response
    * def urls = read('classpath:services/searchRepository/templates/response/urls.json')
    * replace urls
      | token 	    | value    |
      | #(fullName) | fullName |
    * json urls = urls
    And match response.items[0] contains urls


  Scenario: Ensure total_count is equal to the number of repositories in items[]
    * params {q: 'user:daveremy'}
    When method GET
    Then status 200
    * def totalCount = response.total_count

    * params {q: 'user:daveremy', per_page: '#(totalCount)'}
    When method GET
    Then status 200
    And assert response.items.length == totalCount


  @negativeTest
  Scenario: Ensure 422 Validation occurs when query is missing
    * params {q: ''}
    When method GET
    Then status 422
    And match response ==
    """
    {
        "message": "Validation Failed",
        "errors": [
            {
                "resource": "Search",
                "field": "q",
                "code": "missing"
            }
        ],
        "documentation_url": "https://docs.github.com/v3/search"
    }
    """
















