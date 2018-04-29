import * as React from "react";
import { mutate } from "../../src/functions/mutate";

interface ImutateFunction {
  externalLegalName: string;
  externalEmail: string;
}

interface IProps {
  mutate(...ImutateFunction): void;
}

export class CreateNewUserPage extends React.Component<IProps, {}> {
  public startingState = {
    externalLegalName: "",
    externalEmail: ""
  };
  public state = this.startingState;

  public reset = () => this.setState(this.startingState);

  public updateField = stateKey => e => {
    this.setState({ [stateKey]: e.target.value });
  };

  public handelSubmit = e => {
    e.preventDefault();
    return (
      this.props.mutate({
        variables: {
          externalEmail: this.state.externalEmail,
          externalLegalName: this.state.externalLegalName
        }
      }),
      this.setState(this.startingState)
    );
  };

  public render() {
    return (
      <div>
        <form onSubmit={this.handelSubmit}>
          <div className="group">
            <label>Name</label>
            <input
              type="text"
              id="externalLegalName"
              value={this.state.externalLegalName}
              onChange={this.updateField("externalLegalName")}
            />
          </div>

          <div className="group">
            <label>Email</label>
            <input
              type="text"
              id="externalEmail"
              value={this.state.externalEmail}
              onChange={this.updateField("externalEmail")}
            />
          </div>
          <button data-testid="submit-button" type="submit">
            Submit
          </button>
          <button data-testid="reset-button" type="button" onClick={this.reset}>
            Reset
          </button>
        </form>
      </div>
    );
  }
}
