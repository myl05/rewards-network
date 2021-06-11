##
# This is a reusable search repository endpoint. It can be re-used in any other scenarios to mitigate code duplication
# Using @ignore tag ensures this feature will not be run when executing tests
#
# @author mlee
##

@ignore
Feature: Reusable Search Repository endpoint

  Background:
    * def config = {"perPageCount": '#(perPage)'}
    * def perPageCount = config.perPageCount

  Scenario:
    Given url ghURL + 'search/repositories'
    * params {q: '#(query)', per_page: '#(perPage)', page: '#(pageNumber)'}
    When method GET
    Then status 200
    And assert response.items.length == perPageCount