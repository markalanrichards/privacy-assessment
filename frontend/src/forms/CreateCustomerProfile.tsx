import { useState } from 'react';

export function CreateCustomerProfile({ mutation }) {
  const [externalLegalName, setExternalLegalName] = useState('');
  const [externalEmail, setExternalEmail] = useState('');

  return (
    <div>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          mutation.mutate({
            externalLegalName,
            externalEmail,
          });
        }}
      >
        <div className="group">
          <label htmlFor="externalLegalName">Name</label>
          <input
            type="text"
            id="externalLegalName"
            value={externalLegalName}
            onChange={(e) => setExternalLegalName(e.target.value)}
          />
        </div>

        <div className="group">
          <label htmlFor="externalEmail">Email</label>
          <input
            type="text"
            id="externalEmail"
            value={externalEmail}
            onChange={(e) => setExternalEmail(e.target.value)}
          />
        </div>
        <button
          data-testid="submit-button"
          type="submit"
          disabled={!Boolean(externalLegalName.length && externalEmail.length)}
        >
          Submit
        </button>
        <button
          data-testid="reset-button"
          type="button"
          onClick={() => {
            setExternalLegalName('');
            setExternalEmail('');
          }}
        >
          Reset
        </button>
      </form>
    </div>
  );
}
