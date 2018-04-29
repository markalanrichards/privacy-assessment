import * as React from "react";
import { CreateNewUserPage } from "./Form";
import { shallow } from "enzyme";
import * as mockApi from "../../src/functions/mutate";

const simulateChangeOnInput = (wrapper, inputSelector, newValue) => {
  const input = wrapper.find(inputSelector);
  input.simulate("change", {
    target: { value: newValue }
  });
  return wrapper.find(inputSelector);
};
describe("Form", () => {
  beforeEach(() => jest.resetAllMocks());
  it("lets me fill out the form with values", () => {
    const wrapper = shallow(<CreateNewUserPage mutate={mockApi.mutate} />);
    expect(wrapper.find("#externalLegalName").props().value).toEqual("");
    expect(wrapper.find("#externalEmail").props().value).toEqual("");
    const nameInput = simulateChangeOnInput(
      wrapper,
      "#externalLegalName",
      "James"
    );
    const emailInput = simulateChangeOnInput(
      wrapper,
      "#externalEmail",
      "james@test.com"
    );

    expect(nameInput.props().value).toEqual("James");
    expect(emailInput.props().value).toEqual("james@test.com");
    expect(wrapper).toMatchSnapshot();
  });

  it("lets me click the reset button to clear the form", () => {
    const wrapper = shallow(<CreateNewUserPage mutate={mockApi.mutate} />);
    const nameInput = simulateChangeOnInput(
      wrapper,
      "#externalLegalName",
      "James"
    );
    const emailInput = simulateChangeOnInput(
      wrapper,
      "#externalEmail",
      "james@test.com"
    );
    const resetBtn = wrapper.find('button[data-testid="reset-button"]');
    resetBtn.simulate("click");
    expect(wrapper.find("#externalLegalName").props().value).toEqual("");
    expect(wrapper.find("#externalEmail").props().value).toEqual("");
    expect(wrapper).toMatchSnapshot();
  });

  test("data is sumbited when button is pressed", () => {
    jest
      .spyOn(mockApi, "mutate")
      .mockImplementation(() => Promise.resolve({ message: "Saved user!" }));
    const wrapper = shallow(<CreateNewUserPage mutate={mockApi.mutate} />);

    const nameInput = simulateChangeOnInput(
      wrapper,
      "#externalLegalName",
      "James"
    );
    const emailInput = simulateChangeOnInput(
      wrapper,
      "#externalEmail",
      "james@test.com"
    );

    wrapper.find("form").simulate("submit", {
      preventDefault: () => {}
    });

    expect(mockApi.mutate).toHaveBeenCalledWith({
      variables: { externalEmail: "james@test.com", externalLegalName: "James" }
    });
  });
});
