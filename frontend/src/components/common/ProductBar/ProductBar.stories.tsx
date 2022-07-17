import ProductBar from '@/components/common/ProductBar/ProductBar';
import { ComponentStory } from '@storybook/react';
import styled from 'styled-components';

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

export const Default = () => (
  <Template name="아주 예쁜 키보드" isSelected={false} />
);

export const Selected = () => (
  <Template name="아주 예쁜 키보드" isSelected={true} />
);
