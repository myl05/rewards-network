##
# This feature is intended to include common parameters shared between all other feature files.
# Using @ignore tag ensures this feature will not be run when executing tests
#
# @author mlee
##

@ignore
Feature:

  Scenario:
    * def waitTime = 2000
    * def wait = function(){java.lang.Thread.sleep(waitTime)}
    * configure abortedStepsShouldPass = true
