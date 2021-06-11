@searchRepo @pagination
Feature: Search Repository Service - Tests involving the page or per_page filter

  Background:
    * callonce read(globalCommonPath)
    * url ghURL + 'search/repositories'

#  Scenario Outline: Ensure correct number of pages are returned based on per_page param: <scenario>
#    * params {q: '<query>', sort: '<sortType>', order: '<orderType>', per_page: '<perPage>'}
#    When method <method>
#    Then status <status>
#    And assert response.items.length == '<expected>'
#
#    Examples:
#      | read('classpath:services/searchRepository/testData/' + env + '/perPageLimits.csv') |
#
#
#  Scenario: Ensure the number of repos on the last page are correct based on the default per_page count (30) and total_count
#    # Testing to ensure we have 30 results per page (default), and then on the last page whatever amount is left over
#    * params {q: 'user:daveremy'}
#    When method GET
#    Then status 200
#    # Get the total count from the response
#    * def totalCount = response.total_count
#    # Divide the total count by 30 (default per_page count) then round to the upper integer to get expected page count
#    * def expectedPages = totalCount / 30
#    * def expectedPagesRoundedUp = Math.ceil(expectedPages)
#    # Round to the lower integer and multiply by 30 to calculate the amount to subtract from total count so we can
#    # check to see if the number of repos on the last page is correct
#    * def expectedPagesRoundedDown = Math.floor(expectedPages)
#    * def amountToSubtractFromTotalCount = expectedPagesRoundedDown * 30
#    * def numberOfReposOnLastPage = totalCount - amountToSubtractFromTotalCount
#
#    # Make another request and pass the calculated last page number to check the count of repos on the last page (16)
#    * params {q: 'user:daveremy', page: '#(expectedPagesRoundedUp)'}
#    When method GET
#    Then status 200
#    # Assert that there are the correct amount of repos on the last page (16)
#    And assert response.items.length == numberOfReposOnLastPage


  @reusableFeatureDemo
  Scenario Outline: Ensure repo count for every page is correct based off of the per_page count. <scenario>
    # Eg. 46 total count. If per_page is 10, there should be 5 pages.
    # The first 4 pages should list 10 repos each, and the last page should have 6.
    * params {q: '<query>', per_page: '<perPage>'}
    When method <method>
    Then status <status>
    # Get the total count from the response
    * def totalCount = response.total_count
    # Divide the total count by per_page count then round to the upper integer to get expected page count
    * def expectedPages = totalCount / <perPage>
    * print expectedPages
    # Round up to the upper integer to get an expected page count
    * def expectedPagesRoundedUp = Math.ceil(expectedPages)
    # custom function to dynamically create pageNumber list
    * def createPageNumList = function(x){return {pageNumber: (x + 1)}}
    # taking our expected page count, subtract the last page, dynamically create pageNumber list.
    # karate repeat will perform our reusable feature x amount based on this number
    * def data = karate.repeat(expectedPagesRoundedUp - 1, createPageNumList)
    # karate.append will return similar: [{"pageNumber": 1},{ "pageNumber": 2},{"pageNumber": 3},{"pageNumber": 4},{"perPage": "10"},{"query": "user:daveremy"}]
    * def data = karate.append(data, {perPage: <perPage>}, {query: '<query>'})
    # passing the list 'data' created from the previous step into a reusable feature that hits the search repo api.
    # here I verify every page, except the last, has the correct amount of repos returned per page
    * call read('classpath:common/reusableFeature/searchRepo.feature') data

    # check to see if the number of repos on the last page is correct
    * def expectedPagesRoundedDown = Math.floor(expectedPages)
    * def amountToSubtractFromTotalCount = expectedPagesRoundedDown * <perPage>
    * def numberOfReposOnLastPage = totalCount - amountToSubtractFromTotalCount
    # Make another request and pass the calculated last page number to check the count of repos on the last page
    * params {q: '<query>', page: '#(expectedPagesRoundedUp)', per_page: '<perPage>'}
    When method GET
    Then status 200
    # Assert that there are the correct amount of repos on the last page
    And assert response.items.length == numberOfReposOnLastPage

    Examples:
    | read('classpath:services/searchRepository/testData/' + env + '/perPage.csv') |