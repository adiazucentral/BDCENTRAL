/* jshint -W117, -W030 */
describe('controlremissionRoutes', function () {
    describe('state', function () {
        var view = 'app/modules/samplesmanagement/remission/remission.html';

        beforeEach(function () {
            module('app.remission', bard.fakeToastr);
            bard.inject('$state');
        });

        it('should map state hematologicalcounter to url /remission ', function () {
            expect($state.href('remission', {})).to.equal('/remission');
        });
        it('should map /remission route to hematologicalcounter View template', function () {
            expect($state.get('remission').templateUrl).to.equal(view);
        });
    });
});