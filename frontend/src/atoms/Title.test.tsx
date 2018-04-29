import * as React from "react";
import { shallow, mount } from "enzyme";
import styled, { ThemeProvider } from "styled-components";
import "jest-styled-components";

import { Normal, Super } from "./Title";

const defaultTheme = {
  color: {
    color: "yellow"
  },
  size: {
    large: "10"
  }
};

const shallowWithTheme = (tree, theme) => {
  const context = shallow(<ThemeProvider theme={theme} />)
    .instance()
    // @ts-ignore
    .getChildContext(); // not in enzyme types file GRRRR!!
  return shallow(tree, { context });
};

describe("Title component With Theme", () => {
  test("Normal", () => {
    const wrapper = shallowWithTheme(<Normal>title</Normal>, defaultTheme);
    // throw wrapper.debug()
    expect(wrapper).toMatchSnapshot();
  });

  test("Super", () => {
    const wrapper = shallowWithTheme(<Super>title</Super>, defaultTheme);
    // throw wrapper.debug()
    expect(wrapper).toMatchSnapshot();
  });
});
