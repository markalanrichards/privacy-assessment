import * as React from "react";
import { mount } from "enzyme";

import { Wrapper } from "./Wrapper";

test("Wapper component", () => {
  const wrapper = mount(<Wrapper>Some text</Wrapper>);

  const text = wrapper.find("article").text();

  expect(text).toEqual("Some text");
  expect(wrapper).toMatchSnapshot();
});
