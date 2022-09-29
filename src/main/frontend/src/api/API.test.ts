import LANS_API from './API';

describe('API adapter', () => {
  test('should be able to be instantiated', () => {
    // Given
    const accessToken = 'myAccessToken';

    // When
    const lans = new LANS_API(accessToken);

    // Then
    expect(lans).toBeDefined();
  });
});
