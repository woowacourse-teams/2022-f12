import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

export const ReviewIconWrapper = styled.div<{ size: 'small' | 'large' }>`
  width: ${({ size }) => (size === 'small' ? '20px' : '25px')};
  height: ${({ size }) => (size === 'small' ? '20px' : '25px')};
`;

export const Value = styled.p<{ size: 'small' | 'large' }>`
  font-size: ${({ size }) => (size === 'small' ? '14px' : '24px')};
`;
