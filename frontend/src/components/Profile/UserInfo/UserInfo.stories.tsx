import { ComponentStory } from '@storybook/react';
import styled from 'styled-components';

import UserInfo from '@/components/Profile/UserInfo/UserInfo';

export default {
  component: UserInfo,
  title: 'Components/Profile/UserInfo',
};

const Container = styled.div`
  width: 500px;
`;

const Template: ComponentStory<typeof UserInfo> = (args) => (
  <Container>
    <UserInfo {...args} />
  </Container>
);
const defaultArg: Member = {
  id: 1,
  gitHubId: '아저씨',
  imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
  name: '아저씨',
  careerLevel: 'none',
  jobType: 'frontend',
};

export const Default = () => <Template userData={defaultArg} />;
