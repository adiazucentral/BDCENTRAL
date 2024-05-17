/* jshint -W117, -W030 */
describe('creditnotecombo', function () {
  describe('state', function () {
      var view = 'app/modules/billing/creditnotecombo/creditnotecombo.html';

      beforeEach(function () {
          module('app.creditnotecombo', bard.fakeToastr);
          bard.inject('$state');
      });

      it('should map state hematologicalcounter to url /creditnotecombo ', function () {
          expect($state.href('creditnotecombo', {})).to.equal('/creditnotecombo');
      });
      it('should map /creditnotecombo route to hematologicalcounter View template', function () {
          expect($state.get('creditnotecombo').templateUrl).to.equal(view);
      });
  });
});
