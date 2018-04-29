import * as React from "react";
import { CreateSubJectProfile } from "./CreateSubJectProfile";
import { shallow } from "enzyme";
import * as mockApi from "../functions/mutate";

const simulateChangeOnInput = (wrapper, inputSelector, newValue) => {
  const input = wrapper.find(inputSelector);
  input.simulate("change", {
    target: { value: newValue }
  });
  return wrapper.find(inputSelector);
};
describe("Form", () => {
  beforeEach(() => jest.resetAllMocks());
  test("lets me fill out the form with values", () => {
    const wrapper = shallow(<CreateSubJectProfile mutate={mockApi.mutate} />);
    expect(wrapper.find("#customerProfileId").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectName").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectReference").props().value).toEqual("");
    const customerProfileIdInput = simulateChangeOnInput(
      wrapper,
      "#customerProfileId",
      "james"
    );
    const externalSubjectNameInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectName",
      "james"
    );

    const externalSubjectReferenceInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectReference",
      "james"
    );

    expect(customerProfileIdInput.props().value).toEqual("james");
    expect(externalSubjectNameInput.props().value).toEqual("james");
    expect(externalSubjectReferenceInput.props().value).toEqual("james");
    expect(wrapper).toMatchSnapshot();
  });

  test("lets me click the reset button to clear the form", () => {
    const wrapper = shallow(<CreateSubJectProfile mutate={mockApi.mutate} />);
    expect(wrapper.find("#customerProfileId").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectName").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectReference").props().value).toEqual("");
    const customerProfileIdInput = simulateChangeOnInput(
      wrapper,
      "#customerProfileId",
      "james"
    );
    const externalSubjectNameInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectName",
      "james"
    );

    const externalSubjectReferenceInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectReference",
      "james"
    );
    const resetBtn = wrapper.find('button[data-testid="reset-button"]');
    resetBtn.simulate("click");
    expect(wrapper.find("#customerProfileId").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectName").props().value).toEqual("");
    expect(wrapper.find("#externalSubjectReference").props().value).toEqual("");
    expect(wrapper).toMatchSnapshot();
  });

  test("data is sumbited when button is pressed", () => {
    jest
      .spyOn(mockApi, "mutate")
      .mockImplementation(() => Promise.resolve({ message: "Saved user!" }));
    const wrapper = shallow(<CreateSubJectProfile mutate={mockApi.mutate} />);

    const customerProfileIdInput = simulateChangeOnInput(
      wrapper,
      "#customerProfileId",
      "james"
    );
    const externalSubjectNameInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectName",
      "james"
    );

    const externalSubjectReferenceInput = simulateChangeOnInput(
      wrapper,
      "#externalSubjectReference",
      "james"
    );

    wrapper.find("form").simulate("submit", {
      preventDefault: () => {}
    });

    expect(mockApi.mutate).toHaveBeenCalledWith({
      variables: {
        customerProfileId: "james",
        externalSubjectName: "james",
        externalSubjectReference: "james"
      }
    });
  });
});
