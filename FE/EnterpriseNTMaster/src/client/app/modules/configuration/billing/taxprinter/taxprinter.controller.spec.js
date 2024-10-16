/* jshint -W117, -W030 */
describe('taxprinterController', function() {
  var controller;
  //var people = mockData.getMockPeople();

  beforeEach(function() {
    bard.appModule('app.taxprinter');
    bard.inject('$controller', '$log', '$q', '$rootScope','localStorageService','authService');
  });

  beforeEach(function() {

     sinon.stub(authService,'login').returns($q.when(
     localStorageService.set('Enterprise_NT.authorizationData', {
          authToken: 'eyJhbG',
          userName: 'DEV'
        })
     )); 
    controller = $controller('taxprinterController');

  });

  describe('taxprinter controller', function() {
    it('should be created successfully', function() {
      expect(controller).to.be.defined;
    });

  describe('after activate', function() {
     it('should have title of taxprinter', function() {
       expect(controller.title).to.equal('taxprinter');
      });

    });
  });
});
