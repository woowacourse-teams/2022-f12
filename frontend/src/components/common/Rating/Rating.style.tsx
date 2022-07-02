import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 0.4rem;
`;

const sizeOptions = {
  small: { unit: 14, value: 16 },
  medium: { unit: 16, value: 20 },
  large: { unit: 20, value: 24 },
};

export const Unit = styled.div<{ size: 'small' | 'medium' | 'large' }>`
  ${({ size }) =>
    `width: ${sizeOptions[size].unit}px; height: ${sizeOptions[size].unit}px;`}
`;

export const Value = styled.div<{ size: 'small' | 'medium' | 'large' }>`
  font-size: ${({ size }) => sizeOptions[size].value}px;
`;
