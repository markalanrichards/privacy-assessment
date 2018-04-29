import {
  GraphQLID,
  GraphQLInputObjectType,
  GraphQLInt,
  GraphQLNonNull,
  GraphQLObjectType,
  GraphQLString
} from "graphql";

const piaAnnex1Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question3: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question4: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question5: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question6: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question7: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex1 = new GraphQLInputObjectType({
  name: "inputPiaAnnex1",
  description: "",
  fields: piaAnnex1Fields
});
const outputPiaAnnex1 = new GraphQLObjectType({
  name: "outputPiaAnnex1",
  description: "",
  fields: piaAnnex1Fields
});
const piaAnnex2Step3Fields = {
  field1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field3: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field4: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex2Step3 = new GraphQLInputObjectType({
  name: "inputPiaAnnex2Step3",
  description: "",
  fields: piaAnnex2Step3Fields
});
const outputPiaAnnex2Step3 = new GraphQLObjectType({
  name: "outputPiaAnnex2Step3",
  description: "",
  fields: piaAnnex2Step3Fields
});
const piaAnnex2Step4Fields = {
  field1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field3: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field4: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex2Step4 = new GraphQLInputObjectType({
  name: "inputPiaAnnex2Step4",
  description: "",
  fields: piaAnnex2Step4Fields
});
const outputPiaAnnex2Step4 = new GraphQLObjectType({
  name: "outputPiaAnnex2Step4",
  description: "",
  fields: piaAnnex2Step4Fields
});
const pitAnnex2Step5Fields = {
  field1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field3: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex2Step5 = new GraphQLInputObjectType({
  name: "inputPiaAnnex2Step5",
  description: "",
  fields: pitAnnex2Step5Fields
});
const outputPiaAnnex2Step5 = new GraphQLObjectType({
  name: "outputPiaAnnex2Step5",
  description: "",
  fields: pitAnnex2Step5Fields
});
const piaAnnex2Step6Fields = {
  field1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  field3: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex2Step6 = new GraphQLInputObjectType({
  name: "inputPiaAnnex2Step6",
  description: "",
  fields: piaAnnex2Step6Fields
});
const outputPiaAnnex2Step6 = new GraphQLObjectType({
  name: "outputPiaAnnex2Step6",
  description: "",
  fields: piaAnnex2Step6Fields
});
const inputAnnex2 = new GraphQLInputObjectType({
  name: "inputAnnex2",
  description: "",
  fields: {
    step1: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step2Part1: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step2Part2: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step3: {
      type: new GraphQLNonNull(inputPiaAnnex2Step3)
    },
    step4: {
      type: new GraphQLNonNull(inputPiaAnnex2Step4)
    },
    step5: {
      type: new GraphQLNonNull(inputPiaAnnex2Step5)
    },
    step6: {
      type: new GraphQLNonNull(inputPiaAnnex2Step6)
    }
  }
});
const outputPiaAnnex2 = new GraphQLObjectType({
  name: "outputPiaAnnex2",
  description: "",
  fields: {
    step1: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step2Part1: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step2Part2: {
      type: new GraphQLNonNull(GraphQLString)
    },
    step3: {
      type: new GraphQLNonNull(outputPiaAnnex2Step3)
    },
    step4: {
      type: new GraphQLNonNull(outputPiaAnnex2Step4)
    },
    step5: {
      type: new GraphQLNonNull(outputPiaAnnex2Step5)
    },
    step6: {
      type: new GraphQLNonNull(outputPiaAnnex2Step6)
    }
  }
});
const piaAnnex3Principle1Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question3: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question4: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question5: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question6: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question7: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question8: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question9: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle1 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle1",
  description: "",
  fields: piaAnnex3Principle1Fields
});
const outputPiaAnnex3Principle1 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle1",
  description: "",
  fields: piaAnnex3Principle1Fields
});
const piaAnnex3Principle2Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle2 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle2",
  description: "",
  fields: piaAnnex3Principle2Fields
});
const outputPiaAnnex3Principle2 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle2",
  description: "",
  fields: piaAnnex3Principle2Fields
});
const piaAnnex3Principle3Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle3 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle3",
  description: "",
  fields: piaAnnex3Principle3Fields
});
const outputPiaAnnex3Principle3 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle3",
  description: "",
  fields: piaAnnex3Principle3Fields
});
const piaAnnex3Principle4Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle4 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle4",
  description: "",
  fields: piaAnnex3Principle4Fields
});
const outputPiaAnnex3Principle4 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle4",
  description: "",
  fields: piaAnnex3Principle4Fields
});
const piaAnnex3Principle5Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle5 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle5",
  description: "",
  fields: piaAnnex3Principle5Fields
});
const outputPiaAnnex3Principle5 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle5",
  description: "",
  fields: piaAnnex3Principle5Fields
});
const piaAnnex3Principle6Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle6 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle6",
  description: "",
  fields: piaAnnex3Principle6Fields
});
const outputPiaAnnex3Principle6 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle6",
  description: "",
  fields: piaAnnex3Principle6Fields
});
const piaAnnex3Principle7Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle7 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle7",
  description: "",
  fields: piaAnnex3Principle7Fields
});
const outputPiaAnnex3Principle7 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle7",
  description: "",
  fields: piaAnnex3Principle7Fields
});
const piaAnnex3Principle8Fields = {
  question1: {
    type: new GraphQLNonNull(GraphQLString)
  },
  question2: {
    type: new GraphQLNonNull(GraphQLString)
  }
};
const inputPiaAnnex3Principle8 = new GraphQLInputObjectType({
  name: "inputPiaAnnex3Principle8",
  description: "",
  fields: piaAnnex3Principle8Fields
});
const outputPiaAnnex3Principle8 = new GraphQLObjectType({
  name: "outputPiaAnnex3Principle8",
  description: "",
  fields: piaAnnex3Principle8Fields
});
const inputAnnex3 = new GraphQLInputObjectType({
  name: "inputAnnex3",
  description: "",
  fields: {
    principle1: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle1)
    },
    principle2: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle2)
    },
    principle3: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle3)
    },
    principle4: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle4)
    },
    principle5: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle5)
    },
    principle6: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle6)
    },
    principle7: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle7)
    },
    principle8: {
      type: new GraphQLNonNull(inputPiaAnnex3Principle8)
    }
  }
});
const outputPiaAnnex3 = new GraphQLObjectType({
  name: "outputAnnex3",
  description: "",
  fields: {
    principle1: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle1)
    },
    principle2: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle2)
    },
    principle3: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle3)
    },
    principle4: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle4)
    },
    principle5: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle5)
    },
    principle6: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle6)
    },
    principle7: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle7)
    },
    principle8: {
      type: new GraphQLNonNull(outputPiaAnnex3Principle8)
    }
  }
});

const inputPiaDocument = new GraphQLInputObjectType({
  name: "inputPiaDocument",
  description: "",
  fields: {
    annex1: {
      type: new GraphQLNonNull(inputPiaAnnex1),
      description: "Mandatory PIA Annex One"
    },
    annex2: {
      type: new GraphQLNonNull(inputAnnex2),
      description: "Mandatory PIA Annex Two"
    },
    annex3: {
      type: new GraphQLNonNull(inputAnnex3),
      description: "Mandatory PIA Annex Three"
    }
  }
});

const outputPiaDocument = new GraphQLObjectType({
  name: "outputPiaDocument",
  description: "",
  fields: {
    annex1: {
      type: new GraphQLNonNull(outputPiaAnnex1),
      description: "Mandatory PIA Annex One"
    },
    annex2: {
      type: new GraphQLNonNull(outputPiaAnnex2),
      description: "Mandatory PIA Annex Two"
    },
    annex3: {
      type: new GraphQLNonNull(outputPiaAnnex3),
      description: "Mandatory PIA Annex Three"
    }
  }
});
export const piaCreateType = new GraphQLInputObjectType({
  name: "piaCreate",
  description: "Create a Privacy Impact Assessment",
  fields: {
    subjectProfileId: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Email for new customer"
    },
    document: {
      type: new GraphQLNonNull(inputPiaDocument),
      description: "Privacy Impact Assessment inputPiaDocument"
    }
  }
});
export const piaType = new GraphQLObjectType({
  name: "pia",
  description: "Privacy Impact assessment",
  fields: {
    id: {
      type: new GraphQLNonNull(GraphQLString),
      description: "ID of customer"
    },
    version: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Version of customer"
    },
    epoch: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Timestamp created"
    },
    subjectProfileId: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Email for new customer"
    },
    document: {
      type: new GraphQLNonNull(outputPiaDocument),
      description: "Privacy Impact Assessment inputPiaDocument"
    }
  }
});
