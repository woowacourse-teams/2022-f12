import UserInfo from '@/components/common/UserInfo/UserInfo';
import sampleProfile from '@/mocks/sample_profile.jpg';
import { ComponentStory } from '@storybook/react';
import styled from 'styled-components';

export default {
  component: UserInfo,
  title: 'Components/UserInfo',
};

const Container = styled.div`
  width: 500px;
`;

const Template: ComponentStory<typeof UserInfo> = (args) => (
  <Container>
    <UserInfo {...args} />
  </Container>
);
const defaultArg = {
  id: '1',
  gitHubId: '아저씨',
  name: '아저씨',
  imageUrl: sampleProfile,
  careerLevel: '경력없음',
  jobType: '프론트엔드',
};

export const Default = () => <Template userData={defaultArg} />;
