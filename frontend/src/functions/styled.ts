import * as styledComponents from "styled-components";

const {
  default: styled,
  css,
  injectGlobal,
  keyframes,
  ThemeProvider
} = styledComponents as styledComponents.ThemedStyledComponentsModule<
  IThemeInterface
>;

interface Color {
  primaryColor: string;
}

interface Size {
  large: string;
}

export interface IThemeInterface {
  color: Color;
  size: Size;
}

export const defaultTheme: IThemeInterface = {
  color: {
    primaryColor: "yellow"
  },
  size: {
    large: "10"
  }
};

export default styled;
export { css, injectGlobal, keyframes, ThemeProvider };
