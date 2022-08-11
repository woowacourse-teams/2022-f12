import { Player } from '@lottiefiles/react-lottie-player';

function ErrorPlaceholder() {
  return (
    <>
      <Player
        autoplay
        loop
        src="https://assets2.lottiefiles.com/datafiles/AP6eAJ4K8cbfOl9/data.json"
        style={{ height: '200px', width: '200px' }}
      />
      <div style={{ width: '100%', textAlign: 'center' }}>오류가 발생했어요..</div>
    </>
  );
}

export default ErrorPlaceholder;
