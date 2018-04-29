import * as React from "react";

import {
  SubjectProfileAvro,
  SubjectProfileCreateAvro,
  SubjectProfileUpdateAvro
} from "../interfaces/SubjectProfile";

interface PropsInterface {
  mutate(...SubjectProfileCreateAvro): void;
}

export class CreateSubJectProfile extends React.Component<
  PropsInterface,
  SubjectProfileCreateAvro
> {
  public state = {
    customerProfileId: "",
    externalSubjectName: "",
    externalSubjectReference: ""
  };

  public reset = () =>
    this.setState({
      customerProfileId: "",
      externalSubjectName: "",
      externalSubjectReference: ""
    });

  public updateField = stateKey => e => {
    this.setState({ [stateKey]: e.target.value });
  };

  public handelSubmit = e => {
    e.preventDefault();
    return (
      this.props.mutate({
        variables: {
          customerProfileId: this.state.customerProfileId,
          externalSubjectName: this.state.externalSubjectName,
          externalSubjectReference: this.state.externalSubjectReference
        }
      }),
      this.reset()
    );
  };

  public render() {
    return (
      <div>
        <form onSubmit={this.handelSubmit}>
          <div className="group">
            <label>customerProfileId</label>
            <input
              type="text"
              id="customerProfileId"
              value={this.state.customerProfileId}
              onChange={this.updateField("customerProfileId")}
            />
          </div>
          <div className="group">
            <label>externalSubjectName</label>
            <input
              type="text"
              id="externalSubjectName"
              value={this.state.externalSubjectName}
              onChange={this.updateField("externalSubjectName")}
            />
          </div>
          <div className="group">
            <label>externalSubjectReference</label>
            <input
              type="text"
              id="externalSubjectReference"
              value={this.state.externalSubjectReference}
              onChange={this.updateField("externalSubjectReference")}
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
