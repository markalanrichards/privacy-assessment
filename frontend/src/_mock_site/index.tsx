import { render } from 'react-dom'
import { QueryClient, QueryClientProvider } from 'react-query'

import { ConnectedNewUserForm } from '../pages/ConnectedNewUserForm'
import { ConnectedCreateSubjectProfile } from '../pages/ConnectedCreateSubjectProfile'

// Log any GraphQL errors or network error that occurred
const queryClient = new QueryClient()

render(
  <QueryClientProvider client={queryClient}>
    <>
      <ConnectedNewUserForm />
      <br />
      <ConnectedCreateSubjectProfile />
    </>
  </QueryClientProvider>,
  document.getElementById('root')
)
