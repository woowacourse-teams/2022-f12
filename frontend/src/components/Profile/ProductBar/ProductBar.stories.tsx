import { ComponentStory } from '@storybook/react';
import styled from 'styled-components';

import ProductBar from '@/components/Profile/ProductBar/ProductBar';

export default {
  component: ProductBar,
  title: 'Components/ProductBar',
};

const Container = styled.div`
  width: 500px;
`;

const Template: ComponentStory<typeof ProductBar> = (args) => (
  <Container>
    <ProductBar {...args} />
  </Container>
);

export const Default = () => <Template name="아주 예쁜 키보드" barType="default" />;

export const Selected = () => <Template name="아주 예쁜 키보드" barType="selected" />;

export const Add = () => <ProductBar.AddButton handleClick={() => alert('클릭 됨')} />;
