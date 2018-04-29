import * as React from "react";
import { shallow, mount } from "enzyme";
import styled, { ThemeProvider } from "styled-components";
import "jest-styled-components";

describe("Unthemed components", () => {
  const Button = styled.button`
    color: red;
  `;

  test("shallow - it works", () => {
    const wrapper = shallow(<Button />);
    expect(wrapper).toMatchSnapshot();
  });

  test("wrapper - it works", () => {
    const wrapper = mount(<Button />);
    expect(wrapper).toMatchSnapshot();
  });
});

test("With Theme", () => {
  const defaultTheme = {
    main: "mediumseagreen"
  };

  const Button = styled.button`
    color: ${props => props.theme.main};
  `;
  const shallowWithTheme = (tree, theme) => {
    const context = shallow(<ThemeProvider theme={theme} />)
      .instance()
      // @ts-ignore
      .getChildContext(); // not in enzyme types file GRRRR!!
    return shallow(tree, { context });
  };

  const wrapper = shallowWithTheme(<Button />, defaultTheme);
  // throw wrapper.debug()
  expect(wrapper).toMatchSnapshot();
});
