import * as React from "react";
import { PIACreateAvro } from "../interfaces/PIA";

interface IProps {
  mutate(...PIACreateAvro): void;
}

export const freshCleanState = {
  subjectProfileId: "",
  document: {
    annex1: {
      question1: "",
      question2: "",
      question3: "",
      question4: "",
      question5: "",
      question6: "",
      question7: ""
    },
    annex2: {
      step1: "",
      step2Part1: "",
      step2Part2: "",
      step3: {
        field1: "",
        field2: "",
        field3: "",
        field4: ""
      },
      step4: {
        field1: "",
        field2: "",
        field3: "",
        field4: ""
      },
      step5: {
        field1: "",
        field2: "",
        field3: ""
      },
      step6: {
        field1: "",
        field2: "",
        field3: ""
      }
    },
    annex3: {
      principle1: {
        question1: "",
        question2: "",
        question3: "",
        question4: "",
        question5: "",
        question6: "",
        question7: "",
        question8: "",
        question9: ""
      },
      principle2: {
        question1: "",
        question2: ""
      },
      principle3: {
        question1: "",
        question2: ""
      },
      principle4: {
        question1: "",
        question2: ""
      },
      principle5: {
        question1: "",
        question2: ""
      },
      principle6: {
        question1: "",
        question2: ""
      },
      principle7: {
        question1: "",
        question2: ""
      },
      principle8: {
        question1: "",
        question2: ""
      }
    }
  }
};
export class CreatePIA extends React.Component<IProps, PIACreateAvro> {
  public state = freshCleanState;

  public reset = () => this.setState(freshCleanState);

  public updateField = stateKey => e => {
    this.setState({ [stateKey]: e.target.value });
  };

  public handelSubmit = e => {
    e.preventDefault();
    return (
      this.props.mutate({
        variables: {
          externalEmail: this.state.subjectProfileId
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
            <label>Name</label>
            <input
              type="text"
              id="subjectProfileId"
              value={this.state.subjectProfileId}
              onChange={this.updateField("subjectProfileId")}
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
