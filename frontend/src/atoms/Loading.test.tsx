import * as React from "react";
import { shallow, mount } from "enzyme";
import { Loading } from "./Loading";

test("error - loads no message", () => {
  const wrapper = shallow(<Loading />);
  expect(wrapper.text()).toBe("Loading...");
  expect(wrapper).toMatchSnapshot();
});
