import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      gap: 0.2rem;
    }
    @media screen and ${device.desktop} {
      gap: 0.4rem;
    }
  `}
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

export const UnreadableValue = styled.span``;

export const ReadableValue = styled.span`
  position: absolute;
  overflow: hidden;
  clip: rect(0 0 0 0);
  height: 1px;
  width: 1px;
  margin: -1px;
  padding: 0;
  border: 0;
`;
