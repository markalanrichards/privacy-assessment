import { useState } from 'react';

export function CreateSubJectProfile({ mutation }) {
  const [customerProfileId, setCustomerProfileId] = useState('');
  const [externalSubjectName, setExternalSubjectName] = useState('');
  const [externalSubjectReference, setExternalSubjectReference] = useState('');

  return (
    <div>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          mutation.mutate({
            customerProfileId,
            externalSubjectName,
            externalSubjectReference,
          });
        }}
      >
        <div className="group">
          <label htmlFor="customerProfileId">customerProfileId</label>
          <input
            type="text"
            id="customerProfileId"
            value={customerProfileId}
            onChange={(e) => setCustomerProfileId(e.target.value)}
          />
        </div>
        <div className="group">
          <label htmlFor="externalSubjectName">externalSubjectName</label>
          <input
            type="text"
            id="externalSubjectName"
            value={externalSubjectName}
            onChange={(e) => setExternalSubjectName(e.target.value)}
          />
        </div>
        <div className="group">
          <label htmlFor="externalSubjectReference">
            externalSubjectReference
          </label>
          <input
            type="text"
            id="externalSubjectReference"
            value={externalSubjectReference}
            onChange={(e) => setExternalSubjectReference(e.target.value)}
          />
        </div>
        <button
          data-testid="submit-button"
          type="submit"
          disabled={
            !Boolean(
              customerProfileId.length &&
                externalSubjectName.length &&
                externalSubjectReference.length
            )
          }
        >
          Submit
        </button>
        <button
          data-testid="reset-button"
          type="button"
          onClick={() => {
            setCustomerProfileId('');
            setExternalSubjectName('');
            setExternalSubjectReference('');
          }}
        >
          Reset
        </button>
      </form>
    </div>
  );
}
