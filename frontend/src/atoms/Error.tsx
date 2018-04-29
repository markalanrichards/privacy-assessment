import * as React from "react";

export interface AtomProps {
  message: string;
  title: string;
}

const Display: React.SFC<AtomProps> = ({ title, message }) => (
  <div>
    <h3>{title}</h3>
    <span>{message}</span>
  </div>
);

export const Error = ({ graphQLErrors, networkError, extraInfo, message }) => (
  <div>
    {/* {console.log({ graphQLErrors, networkError, extraInfo, message })} */}
    <h1>AHhh Error</h1>
    {graphQLErrors
      ? graphQLErrors.map((props, i) => (
          <Display key={i} title="graphQLErrors" {...props} />
        ))
      : null}
    {networkError
      ? networkError.map((props, i) => (
          <Display key={i} title="networkError" {...props} />
        ))
      : null}
    {extraInfo
      ? extraInfo.map((props, i) => (
          <Display key={i} title="extraInfo" {...props} />
        ))
      : null}
    {message ? <Display title="message" message={message} /> : null}
  </div>
);
