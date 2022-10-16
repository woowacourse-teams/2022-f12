import styled from 'styled-components';

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  max-width: 1320px;
  width: 100%;
  margin: 50px auto;
  gap: 3rem;
`;

export const SearchBarWrapper = styled.section`
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 1rem;
`;

export const SRMessageContainer = styled.p`
  overflow: hidden;
  white-space: no-wrap;
  clip: rect(1px, 1px, 1px, 1px);
  clip-path: inset(50%);
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  border: 0;
`;
