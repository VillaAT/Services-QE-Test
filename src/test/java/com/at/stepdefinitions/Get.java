package com.at.stepdefinitions;

import com.at.constants.ApiPaths;
import com.at.models.Curriculum;
import com.at.utils.*;
import com.google.gson.Gson;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.messages.Messages;
import org.junit.Assert;

public class Get {
    private BasicSecurityUtil base= Hooks.getBase();
    private Curriculum mongoCurriculum = Hooks.getCurriculumMongo();
    private Curriculum getResponse;

    /*public Get(BasicSecurityUtil base){

        this.base=base;
    }*/

    @Given("^I am working on \"([^\"]*)\" environment$")
    public void i_am_working_on_environment(String env) throws Exception{
        base.environment=env;
    }
    //rest Asurured

    @Given("^I am targeting \"([^\"]*)\" service$")
    public void i_am_targeting_service(String service) throws Exception{
        base.ServiceApi = new ApiTools(base.environment,service);
    }

    @Given("^I want to retrieve all users$")
    public void i_want_to_retrieve_all_users() throws Exception{
        //the variable has to be refresh since the execution starts the value in null
        mongoCurriculum=Hooks.getCurriculumMongo();
        if(mongoCurriculum==null){
            base.apiResource= ApiPaths.SCE_GET_USERS;
        }else{

            base.apiResource="";
            ApiPaths.setUser_id(mongoCurriculum.getResourceId());
            base.apiResource= ApiPaths.getUserByResourceID;
        }
    }

    @When("I send a GET request")
    public void i_send_a_GET_request() {
        System.out.println(base.ServiceApi.hostName + base.apiResource);
        base.response=base.ServiceApi.retrieve(base.ServiceApi.hostName + base.apiResource);
    }


    @Given("^I want to retrieve a user by his resourceID \"([^\"]*)\"$")
    public void i_want_to_retrieve_all_users(String resourceID) throws Exception{
        base.apiResource="";
        ApiPaths.setUser_id(resourceID);
        base.apiResource= ApiPaths.getUserByResourceID;
    }

    @Then("I get response for GET request")
    public void i_get_response_for_GET_request() {
        String responseBody = base.ServiceApi.response.getBody();
        // convert String to an Json object
        // convert String to a GSON builder - googleAPI- java objects
        Gson response_get = new Gson();
        //.class are use to pass classes as variable
        System.out.println(base.ServiceApi.response.getBody());
        getResponse = response_get.fromJson(responseBody, Curriculum.class);

        System.out.println(getResponse);
    }
    @Then("the status code should be \"([^\"]*)\"")
    public void the_status_code_should_be(String statusCode) {
        int status= Integer.parseInt(statusCode);
        Assert.assertEquals(status,base.ServiceApi.response.getStatusCode().value());
    }

    @Then("^I compare both json response and expect to be similar$")
    public void i_compare_both_json_response_and_expect_to_be_similar() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Response body  Assert Get: "+getResponse);
        System.out.println("Response body  Assert Mongo: "+mongoCurriculum);
        //Assert.assertEquals(true,mongoCurriculum.equals(getResponse));
        Assert.assertEquals(mongoCurriculum,getResponse);
    }

    @Then("information retrieved from service should match with DB")
    public void information_retrieved_from_service_should_match_with_DB() {
        //Add validations to DB

    }

    @And("I set  value on the request")
    public void iSetAsValueOnTheRequest(String field, String value) {
        //ObjectTools.updateField(Object, field, value);
    }

    @And("I delete the field \"([^\"]*)\" ")
    public void iDeleteTheField(String field) {
        //ObjectTools.deleteField(Object, field);
    }


    @And("Verify field  in response")
    public void verifyFieldAsInResponse(String field, String expectedValue) {
        //Assert.assertTrue(ObjectTools.verifyField(Object, field, expectedValue));
    }
}
