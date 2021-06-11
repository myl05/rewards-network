/**
 * The only ‘rule’ is that on start-up Karate expects a file called karate-config.js to exist on the ‘classpath’
 * and contain a JavaScript function. The function is expected to return a JSON object and all keys and values in
 * that JSON object will be made available as script variables.
 * @author mlee
 **/

function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'qa';
  }
  var config = {
    env: env,
    ghURL: "",
    globalCommonPath: 'classpath:common/common.feature'
  }
  if (env == 'qa') {
    config.ghURL = "https://api.github.com/"
  } else if (env == 'dev') {
    // customize
  }
  karate.configure('connectTimeout', 40000);
  karate.configure('readTimeout', 40000);
  karate.configure('retry',{ count: 5, interval: 10000 });
  return config;
}