import * as React from "react";

interface LinkClassProps {
  href: string;
}

export class Link extends React.Component<LinkClassProps> {
  public STATUS = {
    HOVERED: "hovered",
    NORMAL: "normal"
  };
  public state = {
    class: this.STATUS.NORMAL
  };

  public onMouseEnter = () => {
    this.setState({ class: this.STATUS.HOVERED });
  };

  public onMouseLeave = () => {
    this.setState({ class: this.STATUS.NORMAL });
  };

  public render() {
    return (
      <a
        className={this.state.class}
        href={this.props.href}
        onMouseEnter={this.onMouseEnter}
        onMouseLeave={this.onMouseLeave}
      >
        {this.props.children}..
      </a>
    );
  }
}
