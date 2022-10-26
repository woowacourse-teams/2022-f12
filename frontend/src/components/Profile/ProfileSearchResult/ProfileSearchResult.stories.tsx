import styled from 'styled-components';

import ProfileSearchResult from '@/components/Profile/ProfileSearchResult/ProfileSearchResult';

import { members } from '@/mocks/data';

export default {
  component: ProfileSearchResult,
  title: 'Components/Profile/ProfileSearchResult',
};

const Container = styled.div`
  width: 500px;
`;

const Template = (data: ProfileSearchResult[]) => (
  <Container>
    <ProfileSearchResult
      isLoading={false}
      isError={false}
      data={data}
      getNextPage={() => {
        console.log('getting next page');
      }}
    />
  </Container>
);

export const Default = () => Template(members);
