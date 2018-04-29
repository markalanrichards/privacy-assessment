import * as React from "react";
import { shallow, mount } from "enzyme";
import { Error } from "./Error";

test("error - loads no message", () => {
  const mockData = {
    graphQLErrors: null,
    networkError: null,
    extraInfo: null,
    message: null
  };
  const wrapper = shallow(<Error {...mockData} />);
  expect(wrapper.text()).toBe("AHhh Error");
  expect(wrapper).toMatchSnapshot();
});

test("error - graphQLErrors", () => {
  const mockData = {
    graphQLErrors: [{ data: "error" }],
    networkError: null,
    extraInfo: null,
    message: null
  };
  const wrapper = mount(<Error {...mockData} />).find("h3");

  expect(wrapper.text()).toBe("graphQLErrors");
  expect(wrapper).toMatchSnapshot();
});

test("error - networkError", () => {
  const mockData = {
    graphQLErrors: null,
    networkError: [{ data: "error" }],
    extraInfo: null,
    message: null
  };
  const wrapper = mount(<Error {...mockData} />).find("h3");

  expect(wrapper.text()).toBe("networkError");
  expect(wrapper).toMatchSnapshot();
});

test("error - extraInfo", () => {
  const mockData = {
    graphQLErrors: null,
    networkError: null,
    extraInfo: [{ data: "error" }],
    message: null
  };
  const wrapper = mount(<Error {...mockData} />).find("h3");

  expect(wrapper.text()).toBe("extraInfo");
  expect(wrapper).toMatchSnapshot();
});

test("error - message", () => {
  const mockData = {
    graphQLErrors: null,
    networkError: null,
    extraInfo: null,
    message: "error message"
  };
  const wrapper = mount(<Error {...mockData} />);

  expect(wrapper.find("h3").text()).toBe("message");
  expect(wrapper.find("span").text()).toBe("error message");
  expect(wrapper).toMatchSnapshot();
});
