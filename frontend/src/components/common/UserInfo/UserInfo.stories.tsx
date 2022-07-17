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

export const Default = () => (
  <Template
    profileImageUrl={sampleProfile}
    username="@인도아저씨"
    jobType="프론트엔드"
    career="0-2년차"
  />
);
