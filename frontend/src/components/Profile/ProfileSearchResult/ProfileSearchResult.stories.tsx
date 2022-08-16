import { ComponentStory } from '@storybook/react';
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

const Template: ComponentStory<typeof ProfileSearchResult> = (args) => (
  <Container>
    <ProfileSearchResult {...args} />
  </Container>
);

export const Default = (args) => <Template data={members} {...args} />;
