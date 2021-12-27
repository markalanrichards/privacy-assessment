import { render, cleanup, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { CreateSubJectProfile } from '../CreateSubJectProfile';
import * as mockApi from '../../mockFunctions/mutate';

const getTextBoxByName = (name) =>
  screen.getByRole('textbox', {
    name,
  });

describe('Form', () => {
  beforeEach(() => cleanup());
  test('lets me fill out the form with values', async () => {
    render(<CreateSubJectProfile mutation={mockApi} />);

    // one
    const customerProfileId = getTextBoxByName('customerProfileId');

    expect(customerProfileId).toHaveValue('');

    userEvent.type(customerProfileId, 'one');

    expect(customerProfileId).toHaveValue('one');

    // two
    const externalSubjectName = getTextBoxByName('externalSubjectName');

    expect(externalSubjectName).toHaveValue('');
    userEvent.type(externalSubjectName, 'two');

    expect(externalSubjectName).toHaveValue('two');

    // three
    const externalSubjectReference = getTextBoxByName(
      'externalSubjectReference'
    );
    expect(externalSubjectReference).toHaveValue('');
    userEvent.type(externalSubjectReference, 'three');

    expect(externalSubjectReference).toHaveValue('three');
  });

  test('lets me click the reset button to clear the form', () => {
    render(<CreateSubJectProfile mutation={mockApi} />);

    const customerProfileId = getTextBoxByName('customerProfileId');
    const externalSubjectName = getTextBoxByName('externalSubjectName');
    const externalSubjectReference = getTextBoxByName(
      'externalSubjectReference'
    );
    expect(customerProfileId).toHaveValue('');
    expect(externalSubjectName).toHaveValue('');
    expect(externalSubjectReference).toHaveValue('');

    userEvent.type(customerProfileId, 'one');
    userEvent.type(externalSubjectName, 'two');
    userEvent.type(externalSubjectReference, 'three');

    expect(customerProfileId).toHaveValue('one');
    expect(externalSubjectName).toHaveValue('two');
    expect(externalSubjectReference).toHaveValue('three');

    userEvent.click(screen.getByRole('button', { name: 'Reset' }));

    expect(customerProfileId).toHaveValue('');
    expect(externalSubjectName).toHaveValue('');
    expect(externalSubjectReference).toHaveValue('');
  });

  test('data is sumbited when button is pressed', () => {
    jest.spyOn(mockApi, 'mutate');

    render(<CreateSubJectProfile mutation={mockApi} />);

    const customerProfileId = getTextBoxByName('customerProfileId');
    const externalSubjectName = getTextBoxByName('externalSubjectName');
    const externalSubjectReference = getTextBoxByName(
      'externalSubjectReference'
    );

    expect(screen.getByRole('button', { name: 'Submit' })).toBeDisabled();

    userEvent.type(customerProfileId, 'one');
    userEvent.type(externalSubjectName, 'two');
    userEvent.type(externalSubjectReference, 'three');

    expect(screen.getByRole('button', { name: 'Submit' })).toBeEnabled();

    userEvent.click(screen.getByRole('button', { name: 'Submit' }));

    expect(mockApi.mutate).toHaveBeenCalledWith({
      customerProfileId: 'one',
      externalSubjectName: 'two',
      externalSubjectReference: 'three',
    });
  });
});
