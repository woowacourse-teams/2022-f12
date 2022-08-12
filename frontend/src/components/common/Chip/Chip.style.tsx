import styled from 'styled-components';

const chipSize = {
  s: { padding: '0.2rem 0.4rem', fontSize: '0.7rem' },
  l: { padding: '0.5rem', fontSize: '1rem' },
};

export const Container = styled.div<{ size: 's' | 'l' }>`
  width: max-content;

  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 2rem;

  font-weight: 500;

  ${({ size }) => `
  padding: ${chipSize[size].padding};
  font-size: ${chipSize[size].fontSize};
  `}
`;
