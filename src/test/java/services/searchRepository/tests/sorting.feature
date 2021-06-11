##
# @author mlee
##

@searchRepo @sort
Feature: Search Repository Service - Tests involving the sorting filter

  Background:
    * callonce read(globalCommonPath)
    * url ghURL + 'search/repositories'

  Scenario: Sorting: Sort the results by forks and ensure the results are ordered asc
    * params {q: 'user:yanglbme', sort: 'forks', order: 'asc', per_page: '7'}
    When method GET
    Then status 200
    * def forkCountList = $..forks_count
    * def sortingUtil = Java.type('utils.SortedUtil')
    * def result = sortingUtil.isSorted(forkCountList, 'asc')
    And match result == true


  Scenario: Sorting: Sort the results by forks and ensure the results are ordered desc
    * params {q: 'user:yanglbme', sort: 'forks', order: 'desc', per_page: '7'}
    When method GET
    Then status 200
    * def forkCountList = $..forks_count
    * def sortingUtil = Java.type('utils.SortedUtil')
    * def result = sortingUtil.isSorted(forkCountList, 'desc')
    And match result == true


#   Dynamic/data-driven version of the sort-by tests above, as well additinal cases. Data fed from sorting.csv
#   Included sorting: updated, forks, and stars either ascending or descending order
#   TODO Sorting by 'updated' is intentionally failing. This is either a bug, or instead of sorting by 'updated_at'
#   TODO they are sorting by 'pushed_at'
    Scenario Outline: Sorting: <scenario>
      * params {q: '<query>', sort: '<sortType>', order: '<orderType>'}
      When method <method>
      Then status <status>
      # Gather each repo object in the response, extract the count of what's being tested (updated, forks, stars)
      # and store them in a list ($.. is shorthand for response..<title>. <title> is passed dynamically through csv)
      * def updatedAtList = $..<title>
      # Call Java helper class for sorting to ensure the list is sorted. utils/SortedUtil.java
      * def sortingUtil = Java.type('utils.SortedUtil')
      # result will return either true or false from the Java helper class SortedUtil.java
      * def result = sortingUtil.<sortMethod>(updatedAtList, '<orderType>')
      And match result == true

      Examples:
        | read('classpath:services/searchRepository/testData/' + env + '/sorting.csv') |