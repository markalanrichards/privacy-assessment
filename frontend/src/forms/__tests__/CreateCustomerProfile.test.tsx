import { render, cleanup, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { CreateCustomerProfile } from '../CreateCustomerProfile';
import * as mockApi from '../../mockFunctions/mutate';

const getTextBoxByName = (name) =>
  screen.getByRole('textbox', {
    name,
  });

describe('Form', () => {
  beforeEach(() => cleanup());
  it('lets me fill out the form with values', () => {
    render(<CreateCustomerProfile mutation={mockApi} />);

    const customerName = getTextBoxByName('Name');

    expect(customerName).toHaveValue('');
    userEvent.type(customerName, 'one');
    expect(customerName).toHaveValue('one');

    const customerEmail = getTextBoxByName('Email');

    expect(customerEmail).toHaveValue('');
    userEvent.type(customerEmail, 'email@domain.com');
    expect(customerEmail).toHaveValue('email@domain.com');
  });

  it('lets me click the reset button to clear the form', () => {
    render(<CreateCustomerProfile mutation={mockApi} />);

    const customerName = getTextBoxByName('Name');
    userEvent.type(customerName, 'one');

    const customerEmail = getTextBoxByName('Email');
    userEvent.type(customerName, 'email@domain.com');

    userEvent.click(screen.getByRole('button', { name: 'Reset' }));

    expect(customerName).toHaveValue('');
    expect(customerEmail).toHaveValue('');
  });

  test('data is sumbited when button is pressed', () => {
    jest.spyOn(mockApi, 'mutate');

    render(<CreateCustomerProfile mutation={mockApi} />);

    const customerName = getTextBoxByName('Name');
    const customerEmail = getTextBoxByName('Email');
    expect(screen.getByRole('button', { name: 'Submit' })).toBeDisabled();

    userEvent.type(customerName, 'name');
    userEvent.type(customerEmail, 'email@domain.com');

    expect(screen.getByRole('button', { name: 'Submit' })).toBeEnabled();

    userEvent.click(screen.getByRole('button', { name: 'Submit' }));

    expect(mockApi.mutate).toHaveBeenCalledWith({
      externalLegalName: 'name',
      externalEmail: 'email@domain.com',
    });
  });
});
