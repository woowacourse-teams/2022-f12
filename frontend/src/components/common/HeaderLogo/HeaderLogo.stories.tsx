import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import { ComponentStory } from '@storybook/react';

export default {
  component: HeaderLogo,
  title: 'Components/Header',
};

const Template: ComponentStory<typeof HeaderLogo> = () => <HeaderLogo />;

export const Default = () => <Template />;
