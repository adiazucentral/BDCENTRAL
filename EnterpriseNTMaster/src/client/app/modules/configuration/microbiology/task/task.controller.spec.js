/* jshint -W117, -W030 */
describe('TaskController', function () {
    var controller;

    beforeEach(function () {
        bard.appModule('app.task');
        bard.inject('$controller', '$log', '$q', '$rootScope', 'localStorageService', 'authService');
    });

    beforeEach(function () {

        sinon.stub(authService, 'login').returns($q.when(
        localStorageService.set('Enterprise_NT.authorizationData', {
            authToken: 'eyJhbG',
            userName: 'DEV'
        })
        ));
        controller = $controller('TaskController');

    });

    describe(' task controller', function () {
        it('should be created successfully', function () {
            expect(controller).to.be.defined;
        });
        describe('after activate', function () {
            it('should have title of task', function () {
                expect(controller.title).to.equal('Task');
            });
        });
    });
});