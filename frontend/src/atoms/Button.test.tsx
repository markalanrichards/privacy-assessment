import * as React from "react";
import { mount, shallow } from "enzyme";
import styled, { ThemeProvider } from "../functions/styled";
import { Button } from "./Button";

const defaultTheme = {
  color: {
    primaryColor: "yellow"
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
  return mount(tree, {
    context,
    childContextTypes: ThemeProvider.childContextTypes
  });
};

describe("Title component With Theme", () => {
  test("Normal", () => {
    const wrapper = shallowWithTheme(
      <Button title="title" url="https://test.com" />,
      defaultTheme
    );
    expect(wrapper).toMatchSnapshot();
  });
});
