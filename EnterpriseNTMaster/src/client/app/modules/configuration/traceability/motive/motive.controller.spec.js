/* jshint -W117, -W030 */
describe('MotiveController', function () {
    var controller;
    beforeEach(function () {
        bard.appModule('app.motive');
        bard.inject('$controller', '$log', '$q', '$rootScope', 'localStorageService', 'authService');
    });  

    beforeEach(function () {
    
        sinon.stub(authService, 'login').returns($q.when(
        localStorageService.set('Enterprise_NT.authorizationData', {
             authToken: 'eyJhbG',
             userName: 'DEV'
        })
        ));
        controller = $controller('MotiveController');
    
    });
    
    describe('Motive controller', function () {
        it('should be created successfully', function () {
            expect(controller).to.be.defined;
        });
        describe('after activate', function () {
            it('should have title of Role', function () {
                expect(controller.title).to.equal('Motive');
            });
        });
    });
});